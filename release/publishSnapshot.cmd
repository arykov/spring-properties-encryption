@echo off
RMDIR /Q /S spring-properties-encryption || goto :error
git clone git@github.com:arykov/spring-properties-encryption.git || goto :error
set /p mvn_repo_user=maven central user:
set /p mvn_repo_pass=maven central pass:
cd spring-properties-encryption || goto :error
call mvn -X -DskipTests deploy
cd ..
echo "Success"
exit 0
:error
echo Failed with error #%errorlevel%.
exit /b %errorlevel%


