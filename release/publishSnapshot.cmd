@echo off
mkdir spring-properties-encryption
RMDIR /Q /S spring-properties-encryption || goto :error
git clone git@github.com:arykov/spring-properties-encryption.git || goto :error
set /p mvn_repo_user=maven central user:
set /p mvn_repo_pass=maven central pass:
cd spring-properties-encryption || goto :error
call mvn -Dpublish deploy
cd ..
echo "Success"
exit 0
goto :success
:error
echo Failed with error #%errorlevel%.


:success
